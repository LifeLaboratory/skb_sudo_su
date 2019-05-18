# coding=utf-8
from app.api.base import base_name as names
from app.api.src.authentication import auth
from app.api.base.base_router import BaseRouter


class Sales(BaseRouter):

    def __init__(self):
        super().__init__()
        self.args = [names.LOGIN, names.PASSWORD]

    def get(self, id_user):
        args = {
            names.ID_USER: id_user
        }
        get_sales()

    def post(self):
        self._read_args()
        answer = auth(self.data)
        return answer
