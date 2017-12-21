const Controller = require('../../lib/controller');
const eventModel  = require('./event-facade');


class EventController extends Controller {
  find(req, res, next) {
    //console.log(req);
    console.log(res);
    return this.model.find(req.query)
    .then(collection => res.status(200).json({success: true, fulldoc:false, events:collection}))
    .catch(err => next(err));
  }
	

}

module.exports = new EventController(eventModel);
