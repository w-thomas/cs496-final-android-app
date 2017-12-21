const Controller = require('../../lib/controller');
const userModel  = require('./user-facade');
const userSchema  = require('./user-schema');
var jwt = require('jsonwebtoken');
var express = require('express');
var app = express();



class UserController extends Controller {
  
  //Overrides standard controller. This is to add the populate method in the user-facade
  find(req, res, next) {
    // console.log(this);
    console.log(req.decoded._doc._id);
    return this.model.find(req.query)
    .then(collection => res.status(200).json(collection))
    .catch(err => next(err));
  }

  //Find user by ID. populates user's event array with all event data.
  findById(req, res, next) {
    return this.model.findById(req.params.id)
    .then(doc => {
      if (!doc) { return res.status(404).end(); }
      return res.status(200).json(doc);
    })
    .catch(err => next(err));
  }

  //Adds event ID to user document.
  addEvent(req, res, next) {
        console.log(req.body.events);
    console.log(req.decoded._doc._id);

    //TODO: why am I dereferencing the id when it's already in the req object?
    const conditions = { _id: req.params.id };

    this.model.updateEvent(req.decoded._doc._id, req.body.events)
    .then(doc => {
      if (!doc) { return res.status(404).end(); }
      return res.status(200).json({success: true, fulldoc: true, user: doc});
    })
    .catch(err => next(err));
  }

  removeEvent(req, res, next) {
    console.log(req.body.events);
    console.log(req.decoded._doc._id);
    this.model.removeEvent(req.decoded._doc._id, req.body.events)
    .then(doc => {
      if (!doc) { return res.status(404).end(); }
      return res.status(200).json({success: true, user: doc});
    })
    .catch(err => next(err));
  }

  getmydata(req, res, next) {
    console.log(req.body.events);
    console.log(req.decoded._doc._id);
    return this.model.findById(req.decoded._doc._id)
    .then(doc => {
      if (!doc) { return res.status(404).end(); }
      return res.status(200).json({success: true, user: doc});
    })
    .catch(err => next(err));
  }

  //User registration 
  register(req, res, next) {
    if (req.body.email &&
    req.body.name &&
    req.body.password &&
    req.body.confirmPassword) {
      
      if (req.body.password !== req.body.confirmPassword) {
        var err = new Error('Passwords do not match.');
        err.status = 400;
        return next(err);
      }

      var userData = new userSchema({
        email: req.body.email,
        name: req.body.name,
        password: req.body.password
      });

      userData.save(function(err) {
        if (err) {
          return next(err);
        }
        res.json({success: true});
      });

    } else {
      var err = new Error('All fields required.');
      err.status = 400;
      console.log(err);
      return next(err);
    }
  }

  //User Authentication. Checks that email/password match, and returns a JWT
  //TODO: overall security before deploying final build. Auth errors should always be generic to prevent
  //enabling hacks. DO NOT return full user object in JWT -- which elements make sense?
  authenticate(req, res, next) {
    //console.log(req.body);
    userSchema.findOne({
      email: req.body.email
    }, function(err, user) {
      if (err) throw err;

      //console.log(user);
      if (!user) {
        res.json({success:false, message: 'Authentication failed. User not found'});
      } else if (user) {
        if (user.password != req.body.password) {
          res.json({success:false, message: 'Authentication failed. Invalid Password'});
        } else {
          
          var token = jwt.sign(user, 'thisisasecretmessage', {
            // expiresInMinutes: 1440 // expires in 24 hours
          });

          res.json({success: true, message: 'JWT will go here', token: token});
        }
      }
    });
  }
}

module.exports = new UserController(userModel);
