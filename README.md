# Bitcoin explorer (via Blockchain free REST API)

## Description

Sample application developted to illustrate some of the core concepts surrounding
software development:
1. Spring (boot) is powerful
2. With great power comes great _~~BeanNotOfRequiredTypeException~~_ responsability.
3. Simple testing of components - both unit and integration (even though
integration should be executed in an external CI system such as Bamboo/Jenkins)
4. Having fun navigating blockchain terminology and putting it into practice

## Usage

While the application is configurable using standard Spring properties, the default parameters when
ommited should suffice if the user has access to the internet. Custom configuration elements that
, most of the times, won't require change are hosted in

For this, simply run the Main.kt class to start the server up. If you want to
have that extra layer of virtualization, feel free to build and run the
application under Docker using:

```bash
mvn clean install &&
./buildDocker &&
docker run --name BTC-xplorer bitcoin-explorer-via-blockchain-api
```

Default server will be started on port **8080**.

Currently only one endpoint exists.

#### /address/:bitcoin_addr

Returns all unspent transation outputs for the particular

_Example:_ `/address/1Aff4FgrtA1dZDwajmknWTwU2WtwUvfiXa`
```javascript
{
    "outputs": [
        {
            "value": 63871,
            "tx_hash": "db9b6ff6ba4fd5813fe1ae8980ee30645221e333c0f647bb1fc777d0f58d3e23",
            "output_idx": 1
        }
    ]
}
```

## Deployment

Add JAR to your JVM application server of choice or add the Docker image
(after pushing to a Docker registry!) to your prefered container
orchestration service.

## What could be done to make it nicer/more production ready/what I would change

* One thing I am a firm beliver in is that duplication is bad - but especially bad
when documenting. The code should be self-documenting, as in, the documentation for
something would ideally always be wrote in the actual code. Javadocs does this but is
not very flexible for other context that not commenting classes/functions. Since this
is a pure REST API application (mostly), the documentation should be generated based
on the handler that define the endpoints, their inputs and outputs. They ought to be
a tool for this - but I am unaware of it.
    * As such, I normally build them as metadata
    in the Java/Kotlin handler classes using specific annotations and then a service to
    extract these using Reflections in runtime to expose in a webpage. For example,
    the description for the endpoint at the end of `Usage` should really be extracted from
    the handler class itself in order to avoid repetition.
* Not an expert in Spring by any measure, only used it a handful of times
in simple scenarios there might be options that I have taken which are not ideal
for a highly scalable and/or robust system.
    * Have used in several Kotlin/Java microservices [Ratpack](https://ratpack.io/)
    as the HTTP server provider but it requires a bit more scafolding to get such a usable application out of the box.
    For example, it has some issues with resolving static files in /src/main/resources
    when running on a JVM under Docker. Only under Docker. In this scenario, it was
    just faster/easier to get Spring boot up and running.
* Testing! Integration tests should be ran by external systems such as Jenkins or Bamboo.
But I do think it has it uses to host some hybrid semi-integration-unit tests (as in, they actually
use the internet). The unit test themselves could be more complex, but these suffices to allow me
to write this in a TDD fashion.
* I am a big fan of having an thin intermediate layer between the REST API handlers and the actual
delegating handlers (the functions that actually do the work!) that makes it easy
to seamlessly make those same operations available through a messaging bus - ActiveMQ would be
the simplest supplier, ZeroMQ would be the high performance approach.
* The application is quite simple (single endpoint), but more thorough logging
should be employed when dealing with (new) complex operations.
* A simple Dockerfile and build scripts are an honest starting point. However,
nowadays, these type of applications would probably run under an container
orhestration system such as Kubernetes, Docker Swarm or Rancher (well, Rancher
delegates to Kubernetes as of latest version) that would allow the owners to
scale the application horizontaly on demand with more ease (if applicable).
Since the application does not require a lot of configuration these YAML Docker
compose files would be quite straightforward to write.
