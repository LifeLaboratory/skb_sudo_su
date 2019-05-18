from app.api.base.base_sql import Sql


class Provider:
    @staticmethod
    def get_sales_user(args):
        query = """
    select
       n.id_nom
      , n.name
      , n.img
      , n.shelf_life::text
      , n.code
      , sl.count
    from sales_list sl
      left join nomenclature n using ("id_nom")
    where "id_user" = {id_user}
      and not "close"
    order by "date_add"
     """
        # print(query)
        return Sql.exec(query=query, args=args)
