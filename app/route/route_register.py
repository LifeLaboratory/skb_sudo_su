# coding=utf-8
from app.api.base import base_name as names
from app.api.src.registration import register
from app.api.base.base_router import BaseRouter


class Register(BaseRouter):
    def __init__(self):
        super().__init__()
        self.args = [names.LOGIN, names.PASSWORD, names.NAME]

    def post(self):
        self._read_args()
        if ' ' in self.data.get(names.LOGIN) \
            or ' ' in self.data.get(names.PASSWORD) \
            or ' ' in self.data.get(names.NAME):
            return {}
        answer = register(self.data)
        return answer or {}
