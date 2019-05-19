from app.api.base.base_sql import Sql


class Provider:
    @staticmethod
    def get_profile(args):
        query = """
    select name
      , img
    from users
    where "id_user" = {id_user}
    """
        # print(query)
        return Sql.exec(query=query, args=args)
