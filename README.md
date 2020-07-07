#README

## Introdução

Lembrete é um trabalho de PROGMOBILE do primeiro semestre de 2020.  
O App consiste em uma lista de lembretes que o usuário pode criar e deletar.  
Ele possui verificações de formulário e suporta múltiplas contas.  
Também usa a função nativa de alarme do android para efetivar os lembretes.

## Usuários

### Usuário comum

Só existe um tipo de usuário, onde o usuário pode criar e deletar lembretes apenas de sua própria conta 

## Usabilidade

### GIF de demonstração do app

![demo-app](https://i.imgur.com/06m4BYZ.gif)


### Requisitos funcionais

1 - Lembrete com dia anterior  
  Ao usuário inserir uma data anterior ao dia atual, o app não deve permitir a criação de lembretes.  
2 - Senha menor que 5 carácteres  
  Ao usuário tentar inserir uma senha menor que 5 carácteres, o app deve impedir que esse usuário crie uma conta.  
3 - Alarme para o dia atual
  Ao usuário inserir um lembrete para o dia de hoje, o app deve criar automaticamente um alarme para este lembrete.

## Implementações futuras  

1 - Suporte a importacao de alarmes via API
2 - Integração com confirmação de e-mail e recuperar senha  
3 - API remota para guardar dados (alarmes, usuarios)
4 - Criar alarme para qualquer data
5 - Alarmes devem ser opicionais e customizaveis (sons, etc)