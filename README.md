# Api ecommerce
- Gerando uma api com base no arquivo yml.
- Utilizando o site https://editor.swagger.io/, para auxílio e a dependência swaggerCodeGen.
- Neste projeto há um exemplo de configuração, onde são encontrados em /src/main/resources/api.
- Os códigos são disponibilizados na pasta build
- Comando:
```
./gradlew clean build
```

### Projeto
- Demonstra uma outra abordagem para criação de apis.

#### Etag
- hash informado no cabeçalho de resposta, onde possui duas finalidades, cache ou condicional.
- qualquer mudança na entidade de resposta, muda o hash

#### Funcionamento
- cliente solicita um recurso no servidor e recebe no cabeçaho a etag
- cliente solicita novamente o recurso no servidor, informando a etg no cabeçalho na chave if-none-match.
- servidor recebe a requisição e compara o hash da etag enviada com o hash calculo na busca do recurso
- caso os hashs sejam iguais, não enviará nenhuma resposta no corpo da requisição, e emitirará um http stats 304.
