const Model = require('../../lib/facade');
const userSchema  = require('./user-schema');


class UserModel extends Model {

  //Overrides standard find logic to run populate query on the events array
  //Replaces reference _id's with actual documents.
  find(query) {
    return this.Schema
    .find(query).populate('events')
    .exec();
  }

  findById(id) {
    return this.Schema
    .findById(id).populate('events')
    .exec();
  }

  registerUser(userData) {
    console.log(userData);
    return this.Schema
    .create(userData)
    .exec();
  }


  //Conditional is user _id, update value is event _id
  //Pushes event _id into array in user schema.
  updateEvent(conditions, update) {

  	// console.log(conditions);
  	// console.log(update);
	return this.Schema
	.update({_id: conditions}, {$addToSet: { 'events' : update }}, { new: true })
	.exec();
  }	

  removeEvent(conditions, update) {

    // console.log(conditions);
    // console.log(update);
  return this.Schema
    .findOneAndUpdate({_id: conditions}, {$pull: { 'events' : update }}, { new: true })
    .exec();
  } 


}

module.exports = new UserModel(userSchema);
