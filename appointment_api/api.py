import os
from flask import request
from flask_restful import Resource, Api
from db import *
from app import app

api = Api(app)

class Welcome(Resource):
    def get(self):
        return get_all_appointments()

class Appointment(Resource):
    def get(self):
        time = request.args.get("time")
        return get_appointment(time)
    
    def post(self):
        time = request.args.get('time')
        duration = request.args.get('duration')
        location = request.args.get('location')

        appointment = create_appointment(time, duration, location)
        return "appointment creation done!"

    def delete(self):
        time = request.args.get("time")
        return delete_appointment(time)
    
    def put(self):
        old_time = request.args.get("old_time")
        new_time = request.args.get("new_time")
        new_duration = request.args.get("new_duration")
        new_location = request.args.get("new_location")
        return put_appointment(old_time, new_time, new_duration, new_location)



class DeleteAllAppointment(Resource):
    def delete(self):
        return delete_all_appointment()



api.add_resource(Welcome, '/')
api.add_resource(Appointment, '/appointment/')
api.add_resource(DeleteAllAppointment, '/delete_all/')