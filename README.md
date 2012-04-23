# Scrite

A tool to regularly take screen shots and save them, to help me see what the
hell I've been doing all day.

## Usage

Please note that this is currentl linux only. I plan to support windows in the
near future, but I'll need help with OSX since I don't own one. Any
contributions are greatly appreciated.

Clone this repo, create a directory called shots in it, create a new sqlite
database like this

    $ cat init.sql | sqlite3 scrite.db

Then, install leiningen, and do `lein deps` and `lein run`. Watch the screen
shots getting collected in the shots directory and associated data into the
*scrite.db* file. See core.clj for more customization options.

## License

Copyright (C) 2011 Shrikant Sharat Kandula (self@sharats.me)

Distributed under the [MIT license](http://mit.sharats.me).
