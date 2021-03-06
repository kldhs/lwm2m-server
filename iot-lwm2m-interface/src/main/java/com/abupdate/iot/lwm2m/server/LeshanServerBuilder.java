package com.abupdate.iot.lwm2m.server;

import java.net.InetSocketAddress;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.network.config.NetworkConfig.Keys;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
import org.eclipse.lwm2m.core.californium.EndpointFactory;
import org.eclipse.lwm2m.core.node.LwM2mNode;
import org.eclipse.lwm2m.core.node.codec.DefaultLwM2mNodeDecoder;
import org.eclipse.lwm2m.core.node.codec.DefaultLwM2mNodeEncoder;
import org.eclipse.lwm2m.core.node.codec.LwM2mNodeDecoder;
import org.eclipse.lwm2m.core.node.codec.LwM2mNodeEncoder;
import org.eclipse.lwm2m.core.observation.Observation;
import org.eclipse.lwm2m.server.impl.InMemorySecurityStore;
import org.eclipse.lwm2m.server.model.LwM2mModelProvider;
import org.eclipse.lwm2m.server.model.StandardModelProvider;
import org.eclipse.lwm2m.server.registration.Registration;
import org.eclipse.lwm2m.server.registration.RegistrationStore;
import org.eclipse.lwm2m.server.security.Authorizer;
import org.eclipse.lwm2m.server.security.DefaultAuthorizer;
import org.eclipse.lwm2m.server.security.SecurityInfo;
import org.eclipse.lwm2m.server.security.SecurityStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abupdate.iot.lwm2m.impl.CaliforniumRegistrationStore;
import com.abupdate.iot.lwm2m.impl.LwM2mPskStore;
import com.abupdate.iot.lwm2m.start.LwM2m;

/**
 * Class helping you to build and configure a Californium based Leshan Lightweight M2M server. Usage: create it, call
 * the different setters for changing the configuration and then call the {@link #build()} method for creating the
 * {@link LeshanServer} ready to operate.
 */
public class LeshanServerBuilder {
    private static final Logger logger = LoggerFactory.getLogger(LeshanServerBuilder.class);

    private CaliforniumRegistrationStore registrationStore;
    private SecurityStore securityStore;
    private LwM2mModelProvider modelProvider;
    private Authorizer authorizer;

    private InetSocketAddress localAddress;
    private InetSocketAddress localSecureAddress;

    private LwM2mNodeEncoder encoder;
    private LwM2mNodeDecoder decoder;

    private PublicKey publicKey;
    private PrivateKey privateKey;
    private X509Certificate[] certificateChain;
    private Certificate[] trustedCertificates;

    private DtlsConnectorConfig.Builder dtlsConfigBuilder;
    private NetworkConfig coapConfig;
    private EndpointFactory endpointFactory;

    private boolean noSecuredEndpoint;
    private boolean noUnsecuredEndpoint;

    /**
     * <p>
     * Set the address/port for unsecured CoAP Server.
     * </p>
     * <p>
     * By default a wildcard address and the default CoAP port(5683) is used
     *
     * @param hostname The address to bind. If null wildcard address is used.
     * @param port     A valid port value is between 0 and 65535. A port number of zero will let the system pick up an
     *                 ephemeral port in a bind operation.
     */
    public LeshanServerBuilder setLocalAddress(String hostname, int port) {
        if (hostname == null) {
            this.localAddress = new InetSocketAddress(port);
        } else {
            this.localAddress = new InetSocketAddress(hostname, port);
        }
        return this;
    }

    /**
     * <p>
     * Set the address/port for secured CoAP Server (Using DTLS).
     * <p>
     * <p>
     * By default a wildcard address and the default CoAPs port(5684) is used.
     *
     * @param hostname The address to bind. If null wildcard address is used.
     * @param port     A valid port value is between 0 and 65535. A port number of zero will let the system pick up an
     *                 ephemeral port in a bind operation.
     */
    public LeshanServerBuilder setLocalSecureAddress(String hostname, int port) {
        if (hostname == null) {
            this.localSecureAddress = new InetSocketAddress(port);
        } else {
            this.localSecureAddress = new InetSocketAddress(hostname, port);
        }
        return this;
    }

    /**
     * <p>
     * Set the address for unsecured CoAP Server.
     * </p>
     * <p>
     * By default a wildcard address and the default CoAP port(5683) is used.
     */
    public LeshanServerBuilder setLocalAddress(InetSocketAddress localAddress) {
        this.localAddress = localAddress;
        return this;
    }

    /**
     * <p>
     * Set your {@link RegistrationStore} implementation which stores {@link Registration} and {@link Observation}.
     * </p>
     * By default the {@link InMemoryRegistrationStore} implementation is used.
     */
    public LeshanServerBuilder setRegistrationStore(CaliforniumRegistrationStore registrationStore) {
        this.registrationStore = registrationStore;
        return this;
    }

    /**
     * <p>
     * Set your {@link SecurityStore} implementation which stores {@link SecurityInfo}.
     * </p>
     * By default no security store is set. It is needed for secured connection if you are using the defaultAuthorizer
     * or if you want PSK feature activated. An {@link InMemorySecurityStore} is provided to start using secured
     * connection.
     */
    public LeshanServerBuilder setSecurityStore(SecurityStore securityStore) {
        this.securityStore = securityStore;
        return this;
    }

    /**
     * <p>
     * Set your {@link Authorizer} implementation to define if a device if authorize to register to this server.
     * </p>
     * By default the {@link DefaultAuthorizer} implementation is used, it needs a security store to accept secured
     * connection.
     */
    public LeshanServerBuilder setAuthorizer(Authorizer authorizer) {
        this.authorizer = authorizer;
        return this;
    }

    /**
     * <p>
     * Set your {@link LwM2mModelProvider} implementation.
     * </p>
     * By default the {@link StandardModelProvider} implementation is used which support all core objects for all
     * devices.
     */
    public LeshanServerBuilder setObjectModelProvider(LwM2mModelProvider objectModelProvider) {
        this.modelProvider = objectModelProvider;
        return this;
    }

    /**
     * <p>
     * Set the {@link PublicKey} of the server which will be used for RawPublicKey DTLS authentication.
     * </p>
     * This should be used for RPK support only. If you support RPK and X509,
     * {@link LeshanServerBuilder#setCertificateChain(X509Certificate[])} should be used.
     */
    public LeshanServerBuilder setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    /**
     * Set the {@link PrivateKey} of the server which will be used for RawPublicKey(RPK) and X509 DTLS authentication.
     */
    public LeshanServerBuilder setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    /**
     * <p>
     * Set the CertificateChain of the server which will be used for X509 DTLS authentication.
     * </p>
     * For RPK the public key will be extract from the first X509 certificate of the certificate chain. If you only need
     * RPK support, use {@link LeshanServerBuilder#setPublicKey(PublicKey)} instead.
     */
    public LeshanServerBuilder setCertificateChain(X509Certificate[] certificateChain) {
        this.certificateChain = certificateChain;
        return this;
    }

    /**
     * The list of trusted certificates used to authenticate devices.
     */
    public LeshanServerBuilder setTrustedCertificates(Certificate[] trustedCertificates) {
        this.trustedCertificates = trustedCertificates;
        return this;
    }

    /**
     * <p>
     * Set the {@link LwM2mNodeEncoder} which will encode {@link LwM2mNode} with supported content format.
     * </p>
     * By default the {@link DefaultLwM2mNodeEncoder} is used. It supports Text, Opaque, TLV and JSON format.
     */
    public LeshanServerBuilder setEncoder(LwM2mNodeEncoder encoder) {
        this.encoder = encoder;
        return this;
    }

    /**
     * <p>
     * Set the {@link LwM2mNodeDecoder} which will decode data in supported content format to create {@link LwM2mNode}.
     * </p>
     * By default the {@link DefaultLwM2mNodeDecoder} is used. It supports Text, Opaque, TLV and JSON format.
     */
    public LeshanServerBuilder setDecoder(LwM2mNodeDecoder decoder) {
        this.decoder = decoder;
        return this;
    }

    /**
     * Set the Californium/CoAP {@link NetworkConfig}.
     */
    public LeshanServerBuilder setCoapConfig(NetworkConfig config) {
        this.coapConfig = config;
        return this;
    }

    /**
     * Used to create custom CoAP endpoint, this is only for advanced users. <br>
     * DTLSConnector is expected for secured endpoint.
     */
    public LeshanServerBuilder setEndpointFactory(EndpointFactory endpointFactory) {
        this.endpointFactory = endpointFactory;
        return this;
    }

    /**
     * deactivate unsecured CoAP endpoint
     */
    public LeshanServerBuilder disableUnsecuredEndpoint() {
        this.noUnsecuredEndpoint = true;
        return this;
    }

    /**
     * deactivate secured CoAP endpoint (DTLS)
     */
    public LeshanServerBuilder disableSecuredEndpoint() {
        this.noSecuredEndpoint = true;
        return this;
    }

    /**
     * The default Californium/CoAP {@link NetworkConfig} used by the builder.
     */
    public static NetworkConfig createDefaultNetworkConfig() {
        NetworkConfig networkConfig = new NetworkConfig();
        networkConfig.set(Keys.MID_TRACKER, "NULL");
        return networkConfig;
    }

    public LwM2mServer build() {
        if (localAddress == null) {
            localAddress = new InetSocketAddress(LwM2m.DEFAULT_COAP_PORT);
        }
        if (authorizer == null) {
            authorizer = new DefaultAuthorizer(securityStore);
        }
        if (modelProvider == null) {
            modelProvider = new StandardModelProvider();
        }
        if (encoder == null) {
            encoder = new DefaultLwM2mNodeEncoder();
        }
        if (decoder == null) {
            decoder = new DefaultLwM2mNodeDecoder();
        }
        if (coapConfig == null) {
            coapConfig = createDefaultNetworkConfig();
        }

        // handle dtlsConfig
        DtlsConnectorConfig dtlsConfig = null;
        if (dtlsConfigBuilder == null) {
            dtlsConfigBuilder = new DtlsConnectorConfig.Builder();
        }
        // set default DTLS setting for Leshan unless user change it.
        DtlsConnectorConfig incompleteConfig = dtlsConfigBuilder.getIncompleteConfig();
        // Handle PSK Store
        if (incompleteConfig.getPskStore() == null && securityStore != null) {
            dtlsConfigBuilder.setPskStore(new LwM2mPskStore(this.securityStore, registrationStore));
        } else {
            logger.info("PskStore should be automatically set by Leshan. Using a custom implementation is not advised.");
        }

        // Handle secure address
        if (incompleteConfig.getAddress() == null) {
            if (localSecureAddress == null) {
                localSecureAddress = new InetSocketAddress(LwM2m.DEFAULT_COAP_SECURE_PORT);
            }
            dtlsConfigBuilder.setAddress(localSecureAddress);
        } else if (localSecureAddress != null && !localSecureAddress.equals(incompleteConfig.getAddress())) {
            throw new IllegalStateException(String.format("Configuration conflict between LeshanBuilder and DtlsConnectorConfig.Builder for secure address: %s != %s", localSecureAddress, incompleteConfig.getAddress()));
        }

        // Handle active peers
        if (incompleteConfig.getMaxConnections() == null) {
            dtlsConfigBuilder.setMaxConnections(coapConfig.getInt(Keys.MAX_ACTIVE_PEERS));
        }
        if (incompleteConfig.getStaleConnectionThreshold() == null) {
            dtlsConfigBuilder.setStaleConnectionThreshold(coapConfig.getLong(Keys.MAX_PEER_INACTIVITY_PERIOD));
        }

        // handle trusted certificates
        if (trustedCertificates != null) {
            if (incompleteConfig.getTrustStore() == null) {
                dtlsConfigBuilder.setTrustStore(trustedCertificates);
            } else if (!Arrays.equals(trustedCertificates, incompleteConfig.getTrustStore())) {
                throw new IllegalStateException(String.format("Configuration conflict between LeshanBuilder and DtlsConnectorConfig.Builder for trusted Certificates (trustStore) : \n%s != \n%s", Arrays.toString(trustedCertificates), Arrays.toString(incompleteConfig.getTrustStore())));
            }
        }

        // check conflict for private key
        if (privateKey != null) {
            if (incompleteConfig.getPrivateKey() != null && !incompleteConfig.getPrivateKey().equals(privateKey)) {
                throw new IllegalStateException(String.format("Configuration conflict between LeshanBuilder and DtlsConnectorConfig.Builder for private key: %s != %s", privateKey, incompleteConfig.getPrivateKey()));
            }

            // if in raw key mode and not in X.509 set the raw keys
            if (certificateChain == null && publicKey != null) {
                if (incompleteConfig.getPublicKey() != null && !incompleteConfig.getPublicKey().equals(publicKey)) {
                    throw new IllegalStateException(String.format("Configuration conflict between LeshanBuilder and DtlsConnectorConfig.Builder for public key: %s != %s", publicKey, incompleteConfig.getPublicKey()));
                }
                dtlsConfigBuilder.setIdentity(privateKey, publicKey);
            }
            // if in X.509 mode set the private key, certificate chain, public key is extracted from the certificate
            if (certificateChain != null && certificateChain.length > 0) {
                if (incompleteConfig.getCertificateChain() != null && !Arrays.equals(incompleteConfig.getCertificateChain(), certificateChain)) {
                    throw new IllegalStateException(String.format("Configuration conflict between LeshanBuilder and DtlsConnectorConfig.Builder for certificate chain: %s != %s", certificateChain, incompleteConfig.getCertificateChain()));
                }
                dtlsConfigBuilder.setIdentity(privateKey, certificateChain, false);
            }
        }
        dtlsConfig = dtlsConfigBuilder.build();

        // create endpoints
        CoapEndpoint unsecuredEndpoint = null;
        if (!noUnsecuredEndpoint) {
            if (endpointFactory != null) {
                unsecuredEndpoint = endpointFactory.createUnsecuredEndpoint(localAddress, coapConfig, registrationStore);
            } else {
                unsecuredEndpoint = new CoapEndpoint(localAddress, coapConfig, registrationStore);
            }
        }

        CoapEndpoint securedEndpoint = null;
        if (!noSecuredEndpoint && dtlsConfig.getSupportedCipherSuites().length > 0) {
            if (endpointFactory != null) {
                securedEndpoint = endpointFactory.createSecuredEndpoint(dtlsConfig, coapConfig, registrationStore);
            } else {
                securedEndpoint = new CoapEndpoint(new DTLSConnector(dtlsConfig), coapConfig, registrationStore, null);
            }
        }

        if (securedEndpoint == null && unsecuredEndpoint == null) {
            throw new IllegalStateException("All CoAP enpoints are deactivated, at least one endpoint should be activated");
        }
        //????????????-->4
        return new LwM2mServer(unsecuredEndpoint, securedEndpoint, registrationStore, securityStore, authorizer, modelProvider, encoder, decoder, coapConfig);
    }
}
