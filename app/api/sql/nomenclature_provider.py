from app.api.base.base_sql import Sql


class Provider:
    @staticmethod
    def get_list(args):
        query = """
  select *
  from(
    select *
    from nomenclature
    order by name
  ) nom
  limit 100 offset 100*{page}
  """
        # print(query)
        return Sql.exec(query=query, args=args)

    @staticmethod
    def get_user_list(args):
        query = """
 select *
  from(
    select 
      n.name
      , n.code
      , dn.*
    from user_nom 
      left join nomenclature n using(id_nom)
      left join description_nom dn using(id_nom)
    order by name
  ) nom
  limit 100 offset 100*{page}
  """
        # print(query)
        return Sql.exec(query=query, args=args)
