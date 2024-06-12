#!/bin/bash

target_dir="server/target"
client_dir="client/target"
temp_dir="tmp"

mvn clean install

mkdir -p "$temp_dir"

cp "$target_dir/tpe2-g12-server-1.0-SNAPSHOT-bin.tar.gz" "$temp_dir/"
cp "$client_dir/tpe2-g12-client-1.0-SNAPSHOT-bin.tar.gz" "$temp_dir/"
cd "$temp_dir"

# Server
tar -xzf "tpe2-g12-server-1.0-SNAPSHOT-bin.tar.gz"
chmod +x tpe2-g12-server-1.0-SNAPSHOT/run-server.sh
sed -i -e 's/\r$//' tpe2-g12-server-1.0-SNAPSHOT/run-server.sh
rm "tpe2-g12-server-1.0-SNAPSHOT-bin.tar.gz"

# Client
tar -xzf "tpe2-g12-client-1.0-SNAPSHOT-bin.tar.gz"
chmod +x tpe2-g12-client-1.0-SNAPSHOT/query*
sed -i -e 's/\r$//' tpe2-g12-client-1.0-SNAPSHOT/query*
rm "tpe2-g12-client-1.0-SNAPSHOT-bin.tar.gz"