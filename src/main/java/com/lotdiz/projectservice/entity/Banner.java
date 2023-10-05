package com.lotdiz.projectservice.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
@Table(name="banner")
public class Banner {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banner_id")
    private Long bannerId;

    @Column(name = "banner_image_url", nullable = false)
    private String bannerImageUrl;

    @Column(name = "banner_url", nullable = false)
    private String bannerUrl;

}
