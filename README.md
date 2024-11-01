### Create DB Container
  
``` bash
docker run --name artchess -e POSTGRES_PASSWORD=postgres -d -p 5432:5432 postgres
```

### Start Server REPL

``` bash
clj -J-Dclojure.server.repl="{:port 5555 :accept clojure.core.server/repl}"
```

### Start Shadow App

```  bash
yarn dev
```

