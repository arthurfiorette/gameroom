
#
# Build
#
FROM rust:1.55 as build

LABEL maintainer="Arthur Fiorette docker@arthur.place"

WORKDIR /gameroom

# Copy Source
COPY . .

# Cache dependencies
RUN cargo install --path .

# Build for release
RUN rm ./target/release/deps/gameroom*
RUN cargo build --release

#
# RUN
#
FROM debian:buster-slim

LABEL maintainer="Arthur Fiorette docker@arthur.place"

WORKDIR /gameroom

COPY --from=build /gameroom/target/release/gameroom .
COPY .env .

CMD ["./gameroom"]