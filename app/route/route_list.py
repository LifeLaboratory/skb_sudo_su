from app.route.route_auth import Auth
from app.route.route_favicon import Favicon
from app.route.route_register import Register
from app.route.route_info import Info
from app.route.route_nomenclature import Nomenclature
from app.route.route_search import Search
from app.route.route_session import Session
from app.route.route_sales import Sales


ROUTES = {
    Register: '/register',
    Auth: '/auth',
    Search: '/search/{text:field}/{text:query}',
    Info: '/info/{articul:text}',
    Nomenclature: '/get_list/{text:type}/{text:id_user}',
    Favicon: '/favicon.ico',
    Sales: '/sales',
    Session: '/session'
}
