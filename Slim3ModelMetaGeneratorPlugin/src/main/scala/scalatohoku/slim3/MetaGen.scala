package scalatohoku.slim3

class MetaGen(val packageName: String, val modelName: String) {
  val props = new scala.collection.mutable.HashMap[String, String] // name -> className
  val annotations = new collection.mutable.HashMap[String, List[String]]

  def generateSourceFile() {
    import java.io._
    val metaSrcDir = "src/main/scala/" + packageName.replace('.', '/') + "/meta"
    val dir = new File(metaSrcDir)
    if (!dir.exists) dir.mkdirs
    val file = new FileWriter(metaSrcDir + "/" + modelName + "Meta.scala")
    file.write(contents)
    file.close
  }

  private def attribute(prop: String, k: String): String = {
    annotations.get(prop).flatMap {
      _.find(_.startsWith("org.slim3.datastore.Attribute")).flatMap { a =>
        System.out.println(a)
        val re = """.*%s = ([^,]+).*\)""".format(k).r
        a match {
          case re(v) => Some(v.trim)
          case _ => None
        }
      }
    } getOrElse ""
  }

  private def contents: String = {
    val valsSrc = props.keys.map(k => props(k) match {
      case "String" =>
        val unindexed = if (attribute(k, "unindexed") == "true") "Unindexed" else ""
        MetaTemplate.Attr.String.replace("$$propname$$", k).replace("$$unindexed$$", unindexed)
      case "com.google.appengine.api.datastore.Key" =>
        MetaTemplate.Attr.Key
      case clazz =>
        val unindexed = if (attribute(k, "unindexed") == "true") "Unindexed" else ""
        MetaTemplate.Attr.Core.replace("$$propname$$", k).replace("$$typename$$", clazz).replace("$$unindexed$$", unindexed)
    })

    var prePutSrc = ""
    var staticSrc = ""
    props.keys.foreach { k =>
      val re = """classOf\[(.+)\]""".r
      attribute(k, "listener") match {
        case re(v) =>
          if (prePutSrc == "") prePutSrc = MetaTemplate.PrePutHeader
          prePutSrc += MetaTemplate.PrePut.replace("$$modelname$$", modelName).replace("$$propname$$", k).replace("$$getter$$", toGetter(k)).replace("$$setter$$", toSetter(k))
          staticSrc += MetaTemplate.AttributeListener.replace("$$propname$$", k).replace("$$listener$$", v)
        case e =>
          println("no listener found: " + e)
      }
    }

    val modelToEntitySrc = props.keys.map(k =>
      (props(k) match {
        case "com.google.appengine.api.datastore.Text" | "com.google.appengine.api.datastore.Blob" =>
          MetaTemplate.ModelToEntity.Unindexed
        case _ => MetaTemplate.ModelToEntity.Indexed
      }).replace("$$propname$$", k).replace("$$getter$$", toGetter(k)))

    val entityToModelSrc = props.keys.map(k =>
      MetaTemplate.EntityToModel.replace("$$propname$$", k).replace("$$setter$$", toSetter(k)).replace("$$typename$$", props(k)))

    val modelToJsonSrc = props.keys.map(k =>
      MetaTemplate.ModelToJson.replace("$$propname$$", k).replace("$$getter$$", toGetter(k)))

    val jsonToModelSrc = props.keys.map(k =>
      MetaTemplate.JsonToModel.replace("$$propname$$", k).replace("$$getter$$", toGetter(k)).replace("$$setter$$", toSetter(k)))
    MetaTemplate.MetaClass
      .replace("$$instance_variables$$", valsSrc.mkString)
      .replace("$$entity_to_model$$", entityToModelSrc.mkString)
      .replace("$$model_to_entity$$", modelToEntitySrc.mkString)
      .replace("$$model_to_json$$", modelToJsonSrc.mkString)
      .replace("$$json_to_model$$", jsonToModelSrc.mkString)
      .replace("$$pre_put$$", prePutSrc)
      .replace("$$modelname$$", modelName)
      .replace("$$package$$", packageName)
      .replace("$$static$$", staticSrc)
  }

  private def toGetter(s: String) = "get" + toCamelCase(s)
  private def toSetter(s: String) = "set" + toCamelCase(s)
  private def toCamelCase(s: String) = s.substring(0, 1).toUpperCase() + s.substring(1)

}