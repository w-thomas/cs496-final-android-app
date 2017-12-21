const Router = require('express').Router;
const router = new Router();

const user  = require('./model/user/user-router');
const event  = require('./model/event/event-router');


router.route('/').get((req, res) => {
  res.json({ message: 'Welcome to assignment3 API!' });
});

router.use('/user', user);
router.use('/event', event);


module.exports = router;
