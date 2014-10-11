# richobject

`richobject` provides a spring-data repository for constructing repositories that provide rich objects,
with dependency-injected collaborators that allow them to take part in the full range of object-oriented interactions.

## motivation

Technologies like JPA, JAXB, and friends all make it very easy to turn our objects into something we can send on the
wire, or put in a database, or otherwise represent in a remotely-recreatable-form. The problem is that they also teach
us the bad habit of making bare-bones value objects, that are easy to serialize, but don't actually do anything. As
a result, we build systems whose domain-layer consists of a group of "service" singletons, which pass around data-objects.
We engage in _data-oriented programming_.

## usage

The tests wire up a functioning `TestDomainObjectRepository`, which is backed by a persistent JPA entity. Take a look
in `RichRepositoryConfiguration` to see how to wire up a `richobject` repository - note that:
* `richobject` assumes your persistence layer is backed by a jpa-repository
* You will need to provide an appropriate `@Scope("prototype")` bean for `rich-object` to be able to provide
instances of your domain class.

# building

Maven is the build-system of choice. Note that some tests are written with scala-test, so maven will grab the
`scala-maven-plugin` for you.

