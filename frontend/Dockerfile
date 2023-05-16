FROM node:lts-alpine

RUN npm install --global pm2

WORKDIR /app

COPY ./package*.json ./
RUN npm install --omit=dev

COPY . .

RUN npm run build

EXPOSE 3000

USER node

CMD [ "pm2-runtime", "npm", "--", "start" ]
