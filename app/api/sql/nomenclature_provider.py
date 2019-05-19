from app.api.base.base_sql import Sql


class Provider:
    @staticmethod
    def get_list(args):
        query = """
  select *
  from(
    select id_nom
     , name
     , img
     , shelf_life::text
     , code
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
      , n.img
      , dn.id_nom
      , dn.gost
      , dn.weight
      , dn.storage_conditions
      , dn.gmo
      , dn.packing
      , dn.energy
      , id_user_nom
      , expired_start::text
      , expired_end::text
      , expired
    from user_nom 
      left join nomenclature n using(id_nom)
      left join description_nom dn using(id_nom)
    where id_user = {id_user}
      and not "close"
    order by expired_end, name
  ) nom
  limit 100 offset 100*{page}
  """
        # print(query)
        return Sql.exec(query=query, args=args)

    @staticmethod
    def get_user_list_expired(args):
        query = """
 select *
  from(
    select 
      n.name
      , n.code
      , n.img
      , dn.id_nom
      , dn.gost
      , dn.weight
      , dn.storage_conditions
      , dn.gmo
      , dn.packing
      , dn.energy
      , expired_start::text
      , expired_end::text
    from user_nom 
      left join nomenclature n using(id_nom)
      left join description_nom dn using(id_nom)
    where id_user = {id_user}
      and not "close"
    order by name
  ) nom
  """
        # print(query)
        return Sql.exec(query=query, args=args)

    @staticmethod
    def add_nom_in_user(args):
        print(args)
        query = """
 insert into user_nom(id_user, id_nom, 
 expired_start, 
 expired_end)
 select 
   {id_user}
   , {id_nom}
   ,'{expired_start}'::timestamp
   , '{expired_start}'::timestamp + (
              select shelf_life 
              from nomenclature 
              where id_nom = {id_nom} 
              limit 1
      )
  """
        # print(query)
        return Sql.exec(query=query, args=args)

    @staticmethod
    def update_expired_end(args):
        query = """
 update user_nom 
 set expired_end = '{expired_end}'::timestamp
 where id_user = {id_user}
   and id_nom = {id_nom}
  """
        # print(query)
        return Sql.exec(query=query, args=args)

    @staticmethod
    def update_expired_start(args):
        query = """
 update user_nom 
 set expired_start = '{expired_start}'::timestamp
 where id_user = {id_user}
   and id_nom = {id_nom}
  """
        # print(query)
        return Sql.exec(query=query, args=args)

    @staticmethod
    def update_expired(args):
        query = """
 update user_nom 
 set expired_start = '{expired_start}'::timestamp
   , expired_end = '{expired_end}'::timestamp
 where id_user = {id_user}
   and id_nom = {id_nom}
  """
        # print(query)
        return Sql.exec(query=query, args=args)

    @staticmethod
    def delete_expired(args):
        query = """
 update user_nom 
 set close = True
 where id_user = {id_user}
   and id_user_nom = {id_user_nom}
  """
        # print(query)
        return Sql.exec(query=query, args=args)

