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
      , sl.id_sales
    from sales_list sl
      left join nomenclature n using ("id_nom")
    where "id_user" = {id_user}
      and not "close"
    order by "date_add"
     """
        # print(query)
        return Sql.exec(query=query, args=args)

    @staticmethod
    def add_sales_user(args):
        query = """
    insert into sales_list("id_user", "id_nom") values ({id_user}, {id_nom})
     """
        return Sql.exec(query=query, args=args)

    @staticmethod
    def delete_sales_user(args):
        query = """
 update sales_list
 set close = True
 where id_user = {id_user}
   and id_sales = {id_sales}
     """
        return Sql.exec(query=query, args=args)

