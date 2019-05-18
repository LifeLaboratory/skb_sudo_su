from app.route.route_auth import Auth
from app.route.route_favicon import Favicon
from app.route.route_register import Register
from app.route.route_info import Info
from app.route.route_nomenclature_list import NomenclatureList
from app.route.route_nomenclature_user import NomenclatureUser
from app.route.route_search import Search
from app.route.route_session import Session
from app.route.route_sales import Sales


ROUTES = {
    Register: '/register',
    Auth: '/auth',
    Search: '/search/<string:field>/<string:query>',
    Info: '/info/<string:id_nom>',
    # Nomenclature: '/get_list/{text:type}/{text:id_user}',
    NomenclatureList: '/get_list/<int:page>',
    NomenclatureUser: '/get_list/<int:page>/<int:id_user>',
    Favicon: '/favicon.ico',
    Sales: '/sales/<int:id_user>',
    Session: '/session'
}
