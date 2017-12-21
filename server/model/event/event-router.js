const controller = require('./event-controller');
var jwt = require('jsonwebtoken');
const Router = require('express').Router;
const router = new Router();

//Middleware to verify JWT. Get's called before any of the other endpoints afterwards.
//ALL event endpoints require authentication.
router.use(function(req, res, next) {

	var token = req.body.token || req.query.token || req.headers['x-access-token'];

	if (token) {

		jwt.verify(token, 'ilovescotchyscotch', function(err, decoded) {
			if (err) {
				return res.json({success: false, message: 'Failed to authenticate token.'});
			} else {
				req.decoded = decoded;
				next();
			}
		});
	} else {

		return res.status(403).send({ 
        	success: false, 
        	message: 'No token provided.' 
		});
	}
});	

router.route('/')
  .get((...args) => controller.find(...args))
  .post((...args) => controller.create(...args));

router.route('/:id')
  .put((...args) => controller.update(...args))
  .get((...args) => controller.findById(...args))
  .delete((...args) => controller.remove(...args));

module.exports = router;
