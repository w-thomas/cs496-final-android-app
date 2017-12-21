const mongoose = require('mongoose');
const Schema   = mongoose.Schema;


const eventSchema = new Schema({
  title: { type: String, required: true },
  body:  { type: String }
});

//Mongoose pre-hook removes any reference to the event document from user documents, when the event
//Is deleted.
eventSchema.pre('remove', function (next) {
    var event = this;
    event.model('User').update(
        { events: event._id}, 
        { $pull: { events: event._id } }, 
        { multi: true }, 
        next
     );
});

module.exports = mongoose.model('Event', eventSchema);
