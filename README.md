# richobject

`richobject` is an experiment to create a spring-data repository for constructing repositories that provide rich objects,
with dependency-injected collaborators that allow them to take part in the full range of object-oriented interactions.

## motivation

Technologies like JPA, JAXB, and friends all make it very easy to turn our objects into something we can send on the
wire, or put in a database, or otherwise represent in a remotely-recreatable-form. This project is an experiment to
see what it looks like if we ask for _objects_ out of our repositories, instead of _piece of data_. The idea being
to reduce the number of layers between our domain logic and our persistence stores - and reduce the friction that gets
in the way of practicing object-oriented programming (rather than passing data between service singletons).

## usage

The tests wire up a functioning `TestDomainObjectRepository`, which is backed by a persistent JPA entity. Take a look
in `RichRepositoryConfiguration` to see how to wire up a `richobject` repository - note that:
* `richobject` assumes your persistence layer is backed by a jpa-repository
* You will need to provide an appropriate `@Scope("prototype")` bean for `rich-object` to be able to provide
instances of your domain class.

# building

Maven is the build-system of choice. Note that some tests are written with scala-test, so maven will grab the
`scala-maven-plugin` for you.

