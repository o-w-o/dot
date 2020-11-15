function prePublish() {
  cd ../npm || echo "npm dir not found!" && exit

  npm i
  npm run build
  npm run release
}

function normalPublish() {
  prePublish
  npm publish
}

function forcePublish() {
  prePublish
  npm publish -f
}

normalPublish