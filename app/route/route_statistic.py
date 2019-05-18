# coding=utf-8
from app.api.base import base_name as names
from app.api.src.statistic import *
from app.api.base.base_router import BaseRouter


class Statistic(BaseRouter):

    def __init__(self):
        super().__init__()
        self.args = [names.LOGIN, names.PASSWORD]
        self.methods = {
            'top': get_top_statistic,
            'all': get_all_statistic
        }

    def get(self, method, id_user, interval):
        args = {
            names.ID_USER: id_user,
            names.INTERVAL: interval
        }
        if method in ['top', 'all']:
            return self.methods[method](args)
        else:
            return {}
