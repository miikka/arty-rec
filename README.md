# arty-rec

**arty-rec** is a Clojure implementation of [Tracery][tracery]. Tracery is a a
tool for creating generative grammars, developed by [Kate Compton][compton].

[tracery]: http://www.crystalcodepalace.com/tracery.html
[tracery.js]: https://github.com/galaxykate/tracery
[compton]: http://www.galaxykate.com

**Status**: Experimental/alpha. Maybe one day it will feature-compatible with
[the original JavaScript implementation][tracery.js], but for now it is what it
is.

Yes, the name is an anagram.

## Usage

```clojure
(require '[arty-rec.core :as arty-rec])

(let [grammar (arty-rec/create {"origin" "The balloons are #color#."
                                "color" ["red" "green" "blue"]})]
   (println (arty-rec/generate grammar)))
```

## License

Copyright © 2018 Miikka Koskinen.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
