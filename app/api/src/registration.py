from app.api.sql.register_provider import Provider


def register(user_data):
    provider = Provider()
    check = provider.check_user(user_data)
    id_user = provider.register_user(user_data)
    return id_user