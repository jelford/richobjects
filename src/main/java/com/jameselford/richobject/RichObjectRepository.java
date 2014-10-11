package com.jameselford.richobject;

import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;

public interface RichObjectRepository<RichObjectType, RichObjectImplementation extends RichObjectType,
                                        EntityType, ID extends Serializable>
        extends CrudRepository<RichObjectType, ID> {

}
