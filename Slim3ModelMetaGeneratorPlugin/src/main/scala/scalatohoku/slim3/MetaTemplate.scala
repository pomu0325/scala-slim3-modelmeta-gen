package scalatohoku.slim3

object MetaTemplate {
  object Attr {
    val String = """
  val $$propname$$ = new String$$unindexed$$AttributeMeta[$$modelname$$](this, "$$propname$$", "$$propname$$")"""
    val Core = """
  val $$propname$$ = new Core$$unindexed$$AttributeMeta[$$modelname$$,$$typename$$](this, "$$propname$$", "$$propname$$",classOf[$$typename$$])"""
    val Key = """
  val key = new CoreAttributeMeta[$$modelname$$,Key](this, "__key__", "key", classOf[Key])"""
    val StringCollection = """
  val $$propname$$ = new org.slim3.datastore.StringCollection$$unindexed$$AttributeMeta[$$modelname$$, $$typename$$](this, "$$propname$$", "$$propname$$", classOf[$$typename$$])"""
    val Collection = """
  val $$propname$$ = new org.slim3.datastore.Collection$$unindexed$$AttributeMeta[$$modelname$$, $$typename$$, $$typename2$$](this, "$$propname$$", "$$propname$$", classOf[$$typename$$])"""
  }
  
  val EntityToModel = """
    model.$$setter$$(entity.getProperty("$$propname$$").asInstanceOf[$$typename$$])"""

  val EntityToModelLob = """
    val _$$propname$$ = blobToSerializable(entity.getProperty("$$propname$$").asInstanceOf[com.google.appengine.api.datastore.Blob])
    model.$$setter$$(_$$propname$$)"""
  
  val EntityToModelLongText = """
    model.$$setter$$(textToString(entity.getProperty("$$propname$$").asInstanceOf[com.google.appengine.api.datastore.Text]))"""
  
  val EntityToModelBlob = """
    model.$$setter$$(blobToBytes(entity.getProperty("$$propname$$").asInstanceOf[com.google.appengine.api.datastore.Blob]))"""
  
  object ModelToEntity {
    val Indexed = """
    entity.setProperty("$$propname$$", m.$$getter$$())"""
    val Unindexed = """
    entity.setUnindexedProperty("$$propname$$", m.$$getter$$())"""
    val LongText = """
    entity.setUnindexedProperty("$$propname$$", stringToText(m.$$getter$$()))"""
    val Blob = """
    entity.setUnindexedProperty("$$propname$$", bytesToBlob(m.$$getter$$()))"""
  }
  
  val ModelToJson = """
    if(m.$$getter$$() != null){
      writer.setNextPropertyName("$$propname$$")
      encoder0.encode(writer, m.$$getter$$())
    }
"""
  val ModelToJsonCollection = """
    if(m.$$getter$$() != null){
      writer.setNextPropertyName("$$propname$$")
      writer.beginArray()
      m.$$getter$$.foreach{encoder0.encode(writer, _)}
      writer.endArray()
    }
"""
  val ModelToJsonBlob = """
    if(m.$$getter$$ != null){
      writer.setNextPropertyName("$$propname$$")
      encoder0.encode(writer, new com.google.appengine.api.datastore.ShortBlob(m.$$getter$$))
    }
"""
  val JsonToModel = """
    reader = rootReader.newObjectReader("$$propname$$");
    m.$$setter$$(decoder0.decode(reader, m.$$getter$$(), classOf[$$clazz$$]));
"""
  val JsonToModelCollection = """
    reader = rootReader.newObjectReader("$$propname$$");
    {
      val elements = new java.util.$$collection$$[$$clazz$$]
      val r = rootReader.newArrayReader("$$propname$$")
      if(r != null){
        reader = r
        val n = r.length
        (0 until r.length).foreach{i =>
          r.setIndex(i)
          val v = decoder0.decode(reader, null.asInstanceOf[$$clazz$$])
          if(v != null){
            elements.add(v)
          }
        }
        m.$$setter$$(elements)
      }
    }
"""
  val JsonToModelBlob = """
    if(m.$$getter$$ != null){
      m.$$setter$$(decoder0.decode(reader, new com.google.appengine.api.datastore.ShortBlob(m.$$getter$$)).getBytes)
    } else {
      val v = decoder0.decode(reader, null.asInstanceOf[com.google.appengine.api.datastore.ShortBlob])
      if(v != null){
        m.$$setter$$(v.getBytes)
      } else {
        m.$$setter$$(null)
      }
    }
"""

  val AttributeListener = """
    val slim3_$$propname$$AttributeListener = new $$listener$$
"""
  val PrePutHeader = """
    val m = model.asInstanceOf[$$modelname$$]
"""
    
  val PrePut = """
    m.$$setter$$($$modelname$$Meta.slim3_$$propname$$AttributeListener.prePut(m.$$getter$$()));
"""
  
  val MetaClass = """// auto generated by ModelMetaGeneratorPlugin
package $$package$$.meta

import $$package$$.model.$$modelname$$
import org.slim3.datastore._
import com.google.appengine.api.datastore._
import collection.JavaConversions._

class $$modelname$$Meta extends ModelMeta[$$modelname$$]("$$modelname$$", classOf[$$modelname$$]) {

$$instance_variables$$
  
  override def entityToModel(entity:Entity) = {
    val model = new $$modelname$$
    model.setKey(entity.getKey());
    model.setVersion(entity.getProperty("version").asInstanceOf[Long])
$$entity_to_model$$
    model
  }

  override def modelToEntity(model:Any) = {
    val m = model.asInstanceOf[$$modelname$$]
    val entity = if ( m.getKey() != null ) { new Entity(m.getKey())} else {new Entity(kind)}
    entity.setProperty("version", m.getVersion());
    entity.setProperty("slim3.schemaVersion", 1);
$$model_to_entity$$
    entity
  }

  override def getKey(model:Any) = {
    model.asInstanceOf[$$modelname$$].getKey();
  }
  override def setKey(model:Any, key:Key):Unit = {
    validateKey(key);
    model.asInstanceOf[$$modelname$$].setKey(key);
  }
  override def getVersion(model:Any):Long = {
    model.asInstanceOf[$$modelname$$].getVersion
  }
  override def assignKeyToModelRefIfNecessary(ds:com.google.appengine.api.datastore.AsyncDatastoreService, model:Any):Unit = {
  }
  override def incrementVersion(model:Any):Unit = {
    val m = model.asInstanceOf[$$modelname$$]
    val version = m.getVersion()
    m.setVersion( version + 1 )
  }
  override def prePut(model:Any):Unit = {
$$pre_put$$
  }
  override def postGet(model:Any):Unit = {
  }
  override def isCipherProperty(propertyName:String) = false

  override def modelToJson(writer:org.slim3.datastore.json.JsonWriter, model:Any, maxDepth:Int, currentDepth:Int) {
    val m = model.asInstanceOf[$$modelname$$]
    writer.beginObject()
    val encoder0 = new org.slim3.datastore.json.Default
$$model_to_json$$
    writer.endObject()
  }

  override def jsonToModel(rootReader:org.slim3.datastore.json.JsonRootReader, maxDepth:Int, currentDepth:Int):$$modelname$$ = {
    val m = new $$modelname$$
    var reader:org.slim3.datastore.json.JsonReader = null
    val decoder0:org.slim3.datastore.json.Default = new org.slim3.datastore.json.Default
$$json_to_model$$
    m
  }

  override def getSchemaVersionName = "slim3.schemaVersion"
  override def getClassHierarchyListName = "slim3.classHierarchyList";
}
object $$modelname$$Meta extends $$modelname$$Meta{
  def get = this
$$static$$
}
"""
}