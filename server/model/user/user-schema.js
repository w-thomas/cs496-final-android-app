const mongoose = require('mongoose');
const Schema   = mongoose.Schema;


//Mongoose de-references the schema objectID object in events.
const userSchema = new Schema({
  name: { type: String, required: true, trim: true },
  email: { type: String, unique: true, trim: true, required: true},
  password: { type: String, required: true},
  events: [{type: mongoose.Schema.ObjectId, ref: 'Event'}]
});

module.exports = mongoose.model('User', userSchema);
