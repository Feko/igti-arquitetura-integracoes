## Análise Crítica - Acme AP
---
## Disciplina de Análise de Integrações - IGTI - Curso Arquitetura de Software
---
### Pontos positivos da abordagem
A abordagem de produzir um conjunto de APIs REST para consulta e extração de dados da base do sistema da empresa AP tem muitos benefícios. Por ser baseado em um padrão aberto de um protocolo bastante difundido, a interoperabilidade do sistema com outros sistemas e tecnologias é bastante facilitada. Permite a evolução de outros sistemas dependentes de maneira isolada, provendo desacoplamento entre este sistema e outros dependentes - a integração agora passa a ser por uma interface clara, de contratos bem definidos, expostos através de OpenAPI / Swagger.

Como falamos de uma arquitetura de serviço, abrem-se as portas para uma nova gama de possibilidades: Queremos criar um sistema web que permita ao cliente consultar suas faturas? E que tal entregar para a gráfica um endpoint para que possa obter os boletos a serem impressos e enviados? Ou então, porque não criar um aplicativo Mobile para nossos operadores de campo que coletam leituras das instalações dos clientes?

Estes webservices tornam esses desenvolvimentos não apenas possíveis, mas uma tarefa leve e livre de burocracias. O que antes iria requerer uma série de reuniões com ambos os times para decidir estratégias de integração, agora basta apresentar a documentação OpenAPI. No máximo, criar uma API Key para o time. Isso libera ambos os times de reuniões desnecessárias, podendo focar no que realmente entrega valor ao negócio.

### ...mas e o objetivo?
Como bem sabemos, em tecnologia não existe bala de prata. Existe a melhor ferramenta para resolver o problema correto. Nem sempre REST será a resposta para nossas perguntas, e para guiar a solução pelo caminho mais adequado, precisamos entender o objetivo a ser atingido, além do contexto em que se enquadra. E neste caso, o enunciado deixa algumas dúvidas no ar.

O enunciado diz: *[...] Para tal, [os clientes] realizam uma integração com a empresa AP em que recuperam as informações consumindo a API e importam para seu banco de dados [...]* - Mas de quantos clientes estamos falando? Se forem pouquíssimos clientes e com um grande volume de dados, REST pode não ser a melhor saída, e a adoção de um protocolo binário pode ser um fator crucial no sucesso do sistema. Se forem muitos clientes, porque eles precisam ter uma cópia de dados para auditoria? Os dados podem mudar, ou a empresa AP não confia que os dados servidos são íntegros o suficiente? Se os dados podem mudar, como os clientes saberão se os dados precisam de atualização? Há uma estratégia de versionamento dos dados vigente, ou ao menos planejada? Ainda sobre acesso as APIs, quão frequente os clientes chamarão a API? Se os clientes optarem por chamarem a API a cada minuto, e tivermos mais de 100.000 clientes, a infraestrutura necessária para servir esse throughput tem um custo alto, teremos retorno deste investimento?

### Pormenores
Muitas informações estão ausentes no enunciado para que seja possível uma análise crítica contundente. Outros pontos são de suma importância, como

- **Confidencialidade:** Não existe um controle em vigor que impossibilite um terceiro, munido apenas de um número de CPF (Informação que infelizmente está amplamente acessível hoje em dia) de ver dados pessoais de outras pessoas.
- **Segurança:** Faltam mecanismos básicos de autenticação e autorização para tornar uma API segura. Ao menos um SSO / OAuth2 / 2FA para evitar um controle próprio de logins/senhas.
- **API Keys:** Como prevenimos que quando uma chave de API for vazada, ela consiga ser inutilizada? E se precisarmos bloquear um cliente?
- **Throttling:** Como vamos nos prevenir de um cliente inadvertidamente disparar 1.000 requisições em um segundo e criar um gargalo em nossos sistemas? Ou então, como nos defendermos de *scrappers* que extraem dados de plataformas abertas para posterior negociação?
- **GDPR / LGPD:** Estamos *compliance* com estas leis? Deveríamos utilizar o CPF como chave?
- **Read-model:** Aparentemente, estamos fornecendo dados diretamente do banco de produção para esta API. Um pico de leitura poderia comprometer toda a estabilidade do sistema, um risco muito alto a se correr. Por que não ter uma réplica somente leitura da base de dados com consistência eventual para ser servido na API?
- **Throughput:** Qual o *target* de performance que devemos ter como base para dizer que a solução é estável? Quantas requisições simultâneas ela deverá conseguir atender? Qual a latência?
- **Event-Driven:** Essa arquitetura de integração não poderia ser orientada a eventos? Uma persistente queue? ou tópico por cliente para ele ter um stream não-volátil de tudo que aconteceu com suas instalações e faturas, onde cada cliente conheceria somente seu tópico?

### Conclusão
Inquestionavelmente a adoção de uma arquitetura como serviço traz melhorias e benefícios em relação a uma arquitetura monolítica. Dependendo da maturidade da empresa, da expertise de seus colaboradores, e de seu portfólio de tecnologias e sistemas, esse passo pode ser considerado uma grande vitória em direção a um futuro mais aberto a evoluções e integrações.

