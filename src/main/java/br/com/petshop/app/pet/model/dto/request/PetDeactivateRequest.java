package br.com.petshop.app.pet.model.dto.request;

import lombok.Builder;

@Builder
record PetDeactivateRequest (String id) {
}
