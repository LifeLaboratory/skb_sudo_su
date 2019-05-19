from app.api.base.base_sql import Sql


class Provider:
    @staticmethod
    def search_nom(args):
        query = """
    select id_nom
     , name
     , img
     , shelf_life::text
     , code
    from nomenclature
    where lower("{field}") like lower('%{query}%'
    )
     """
        # print(query)
        return Sql.exec(query=query, args=args)

    @staticmethod
    def update_category(args):
        sql = """ Update nomenclature 
      set category = '{category}'
      where id_nom = {id_nom}
                """
        # print(sql)
        Sql.exec(query=sql, args=args)