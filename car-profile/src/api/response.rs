use serde::{Deserialize, Serialize};


#[derive(Serialize,Deserialize)]
pub struct APIResponse<T> {
    pub data: T
}

#[derive(Serialize,Deserialize)]
pub struct APIErrorResponse {
   pub code: String,
   pub message: String
}