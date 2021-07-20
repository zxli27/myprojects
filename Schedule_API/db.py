import json
import os
from app import db, app


class Appointment(db.Model):

    id = db.Column(db.Integer, primary_key=True)

    time = db.Column(db.String(100), unique=True) 
    duration = db.Column(db.Integer)
    location = db.Column(db.String(100))

    def __init__(self, time, duration, location):
        self.time = time
        self.duration = duration
        self.location = location

def create_appointment(new_time, new_duration, new_location):

    appointment = Appointment(new_time, new_duration, new_location)
    db.session.add(appointment)
    db.session.commit()
    return appointment

def get_appointment(new_time):
    one = Appointment.query.filter_by(time=new_time).first()
    if one == None:
        return "Cannot find an appointment."
    return {"time": one.time, "duration":one.duration, "location": one.location}

def get_all_appointments():
    appointments = Appointment.query.all()
    list= [{"time": one.time, "duration":one.duration, "location": one.location} for one in appointments]
    return list

def delete_appointment(new_time):
    appointment = Appointment.query.filter_by(time=new_time).first()
    if appointment == None:
        return "The appointment does not exist."
    db.session.delete()
    db.session.commit()
    return "Appointment deletion done."

def delete_all_appointment():
    appointments = Appointment.query.all()
    for one in appointments:
        db.session.delete(one)
        db.session.commit()
    return "All appointments deletion done."
    
def put_appointment(old_time, new_time, new_duration, new_location):
    one = Appointment.query.filter_by(time=old_time).first()
    if one == None:
        return "can't find the old appointment"
    if new_time != None:
        one.time = new_time
    if new_duration != None:
        one.duration = new_duration
    if new_location != None:
        one.location = new_location
    db.session.commit()
    return "Appointment update done."

if not os.path.isfile(f'{app.root_path}/data.db'):
    db.create_all()
