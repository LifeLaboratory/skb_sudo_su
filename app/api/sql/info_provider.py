from app.api.base.base_sql import Sql


class Provider:
    @staticmethod
    def get_info(args):
        query = """
    select 
      dn.id_nom
      , n.name
      , n.code
      , n.img
      , n.shelf_life::text
      , dn.gost
      , dn.weight
      , dn.storage_conditions
      , dn.gmo
      , dn.packing
      , dn.energy
     from nomenclature n
      left join description_nom dn using(id_nom)
    where n."id_nom" = {id_nom}
                """
        return Sql.exec(query=query, args=args)

    @staticmethod
    def get_info_user(args):
        query = """
    select 
      dn.id_nom
      , n.name
      , n.code
      , n.img
      , n.shelf_life::text
      , dn.gost
      , dn.weight
      , dn.storage_conditions
      , dn.gmo
      , dn.packing
      , dn.energy
      , expired_start::text
      , expired_end::text
      , id_user_nom
      , expired
     from user_nom
      left join nomenclature n using(id_nom)
      left join description_nom dn using(id_nom)
    where n."id_nom" = {id_nom}
      and "id_user" = {id_user}
                """
        return Sql.exec(query=query, args=args)
