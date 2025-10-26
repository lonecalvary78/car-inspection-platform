mod api;
mod router;
use std::env;

use router::{router::init_app_router};

#[tokio::main]
async fn main() {
    let app_endpoint = format!("{}:{}",get_server_host_from_env(),get_server_port_from_env());
    let app_router = init_app_router();
    let net_listener = tokio::net::TcpListener::bind(app_endpoint).await.unwrap();
    axum::serve(net_listener, app_router).await.unwrap();
}

fn get_server_host_from_env()->String{
    return env::var("SERVER_HOSTNAME").unwrap_or_else(|_| "0.0.0.0".to_string());
}

fn get_server_port_from_env()->i32 {
   let server_port = env::var("SERVER_PORT").unwrap_or_else(|_| "8080".to_string());
   server_port.parse().unwrap_or(8080)
}
