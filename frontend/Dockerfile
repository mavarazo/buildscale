FROM node:20-alpine

WORKDIR /app

ENV PATH /app/node_modules/.bin:$PATH

COPY package.json .
COPY package-lock.json .
RUN npm install # -g npm@8.9.0

COPY . .

EXPOSE 3000

CMD ["npm", "start"]