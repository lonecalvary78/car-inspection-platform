use axum::{http::{HeaderMap, HeaderName, HeaderValue, StatusCode}, response::IntoResponse, routing::get, Json, Router};
use serde_json::json;

use crate::api::response;

pub fn init_app_router()->Router {
    return Router::new().route("/cars/", get(index))
}

async fn index()->impl IntoResponse {
   let headers = construct_response_headers();
   return (StatusCode::OK, headers, Json(json!(response::APIResponse{
      data: String::from("ok") 
   })));
}

fn construct_response_headers()->HeaderMap {
    let mut headers = HeaderMap::new();
    headers.insert(HeaderName::from_static("x-api-version"), HeaderValue::from_static("1"));
    return  headers;
}