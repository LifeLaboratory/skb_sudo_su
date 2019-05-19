from app.route.route_auth import Auth
from app.route.route_profile import Profile
from app.route.route_favicon import Favicon
from app.route.route_register import Register
from app.route.route_info import Info
from app.route.route_info_user import InfoUser
from app.route.route_nomenclature_list import NomenclatureList
from app.route.route_nomenclature_user import NomenclatureUser
from app.route.route_nomenclature_user_expired import NomenclatureUserExpired
from app.route.route_nomenclature_user_add import NomenclatureUserAdd
from app.route.route_nomenclature_user_delete import NomenclatureUserDelete
from app.route.route_search import Search
from app.route.route_session import Session
from app.route.route_sales import Sales
from app.route.route_sales_insert import SalesInsert
from app.route.route_sales_delete import SalesDelete
from app.route.route_statistic import Statistic


ROUTES = {
    Register: '/register',
    Auth: '/auth',
    Profile: '/profile/<int:id_user>',
    Search: '/search/<string:field>/<string:query>',
    Info: '/info/<int:id_nom>',
    InfoUser: '/info/<int:id_nom>/<int:id_user>',
    NomenclatureList: '/get_list/<int:page>',
    NomenclatureUserAdd: '/nomenclature',
    NomenclatureUserDelete: '/nomenclature/<int:id_user>/<int:id_user_nom>',
    NomenclatureUser: '/get_list/<int:page>/<int:id_user>',
    NomenclatureUserExpired: '/get_list_expired/<int:id_user>',
    Favicon: '/favicon.ico',
    Sales: '/sales/<int:id_user>',
    SalesInsert: '/sales',
    SalesDelete: '/sales/<int:id_user>/<int:id_sales>',
    Session: '/session',
    Statistic: '/statistic/<string:method>/<int:id_user>/<string:interval>',
}
