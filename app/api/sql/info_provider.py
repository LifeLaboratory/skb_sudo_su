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
