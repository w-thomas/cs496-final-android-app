const controller = require('./user-controller');
var jwt = require('jsonwebtoken');
const Router = require('express').Router;
const router = new Router();

//User authentication endpoint. Gives user access JWT
router.route('/authenticate')
	.post((req, res, next) => controller.authenticate(req, res, next));

//User sign-up endpoint
router.route('/register')
  .post((req, res, next) => controller.register(req, res, next));	

//Middleware to verify JWT. Get's called before any of the other endpoints afterwards.
//This means every endpoint besides authenticate and register need a token. 
router.use(function(req, res, next) {

	//Look for token somewhere in body or header of request. (It should come via headers)
	var token = req.body.token || req.query.token || req.headers['x-access-token'];

	// console.log(req.headers);

	if (token) {

		//Verify token -- providing same secret as it was generated with.
		jwt.verify(token, 'thisisasecretmessage', function(err, decoded) {
			if (err) {

				//Bad token -- error returned.
				return res.json({success: false, message: 'Failed to authenticate token.'});
			} else {

				//If successful, add decoded JWT to request body so it can be used in endpoint.
				req.decoded = decoded;
				//console.log(decoded);
				next();
			}
		});
	} else {

		//No token provided at all.
		return res.status(403).send({ 
        	success: false, 
        	message: 'No token provided.' 
		});
	}
});	

//Find all / create bulk endpoints
router.route('/')
  .get((...args) => controller.find(...args))
  .post((...args) => controller.create(...args));

router.route('/mydata')
	.get((req, res, next) => controller.getmydata(req, res, next));

	//add event to user's list endpoint
router.route('/event')
  .put((...args) => controller.addEvent(...args))

  //Should be a DELETE call but there is a known bug with Volley right with empty params on DELETE calls
  .post((...args) => controller.removeEvent(...args));

//User by ID endpoints
router.route('/:id')
  .put((...args) => controller.update(...args))
  .get((...args) => controller.findById(...args))
  .delete((...args) => controller.remove(...args));



 // router.route('/data')
 //   .get((...args) => controller.getpagedata(...args));

module.exports = router;
