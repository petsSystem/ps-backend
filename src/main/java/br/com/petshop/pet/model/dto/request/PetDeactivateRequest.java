package br.com.petshop.pet.model.dto.request;

import lombok.Builder;

@Builder
record PetDeactivateRequest (String id) {
}
