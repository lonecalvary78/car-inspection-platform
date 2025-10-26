FROM rust:slim-bullseye as builder
WORKDIR /app
COPY src ./src
COPY Cargo.toml .
RUN cargo build --release

FROM debian:bullseye
WORKDIR /app
COPY --from=builder /app/target/release/car-profile /app/
ARG SERVER_PORT=8080
ENV SERVER_PORT=${SERVER_PORT}
EXPOSE ${SERVER_PORT}
ENTRYPOINT [ "/app/car-profile" ]