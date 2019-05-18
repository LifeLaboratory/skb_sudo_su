from app.api.base.base_sql import Sql


class Provider:
    @staticmethod
    def get_sales_user(args):
        query = """
    select *
    from sales_list sl
      left join nomenclature n using ("id_nom")
    where "id_user" = '{id_user}'
     """
        # print(query)
        return Sql.exec(query=query, args=args)
