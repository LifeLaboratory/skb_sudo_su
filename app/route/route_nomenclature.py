# coding=utf-8
from app.api.base import base_name as names, base_errors as errors
from app.api.src.authentication import auth
from flask_restful import Resource


class Nomenclature(Resource):
    args = [names.LOGIN, names.PASSWORD, names.PAGE]

    def post(self):
        error, data = self.parse_data()
        if error == errors.OK:
            error, answer = auth(data)
            if error == errors.OK:
                return answer, {'Access-Control-Allow-Origin': '*'}
        return {names.SESSION: None}, {'Access-Control-Allow-Origin': '*'}

    def options(self):
        return "OK", errors.OK, {'Access-Control-Allow-Origin': '*',
                                 'Access-Control-Allow-Methods': 'GET,POST,DELETE,PUT,OPTIONS',
                                 'Access-Control-Allow-Headers': 'X-Requested-With,Content-Type'}
