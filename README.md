[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/i6Gjtc5R)

# Set up Database
1. [Download MariaDB](https://mariadb.com/download-confirmation/?group-name=Community%20Server&release-notes-uri=https%3A%2F%2Fmariadb.com%2Fkb%2Fen%2Fmariadb-11-2-2-release-notes%2F&download-uri=https%3A%2F%2Fdlm.mariadb.com%2F3674051%2FMariaDB%2Fmariadb-11.2.2%2Fwinx64-packages%2Fmariadb-11.2.2-winx64.msi&product-name=Community%20Server&download-size=72.39%20MB)
2. Start MySQL Client and login as root
3. Run the following commands: ```CREATE DATABASE esideal; CREATE USER 'esi'@'localhost' IDENTIFIED BY 'esi'; GRANT ALL PRIVILEGES ON esideal.* TO 'esi'@'localhost';FLUSH PRIVILEGES;```

# Turmas3L

Exemplo de app com três camadas para usar em DSS.

NOTA: Este código foi criado para discussão e edição durante as aulas práticas de DSS, representando uma solução em construção. Como tal, não deverá ser visto como uma solução canónica, ou mesmo acabada. É disponibilizado para auxiliar o processo de estudo. 

Os alunos são encorajados a testar adequadamente o código fornecido e a procurar soluções alternativas, à medida que forem adquirindo mais conhecimentos. Deverão, por exemplo:
- considerar a utilização de transações nos DAOs, onde apropriado (ver slides)
- melhorar o tratamento de erros, definindo excepções apropriadas
- finalizar a implementação de métodos dos DAOs como put e containsKey
- considerar a utilização generalizada de PreparedStatements, para evitar ataques por SQL injection 


## Estrutura de Pastas

O espaço de trabalho contém duas pastas por padrão, onde:
- `src`: a pasta para manter os fontes
- `lib`: a pasta para manter as dependências (neste caso, os drivers JDBC)

Entretanto, os ficheiros de saída compilados serão gerados na pasta `bin`, por omissão.

> Se quiser personalizar a estrutura de pastas, abra `.vscode/settings.json` e atualize as configurações relacionadas lá.

## Gestão de Dependências

A vista `JAVA PROJECTS` permite-lhe gerir as suas dependências. Mais detalhes podem ser encontrados [aqui](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
