FROM  node:21-alpine3.17
WORKDIR /app/online-decompiler
RUN cd /app/online-decompiler

COPY . .

RUN cat package.json
RUN npm install

EXPOSE 8000


CMD ["npm", "run", "dev"]
