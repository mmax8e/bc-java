package org.bouncycastle.jce.provider;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.internal.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.RSASSAPSSparams;
import org.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;

class X509SignatureUtil
{
    private static boolean isAbsentOrEmptyParameters(ASN1Encodable parameters)
    {
        return parameters == null || DERNull.INSTANCE.equals(parameters);
    }

    static void setSignatureParameters(Signature signature, ASN1Encodable params)
        throws NoSuchAlgorithmException, SignatureException, InvalidKeyException
    {
        if (!isAbsentOrEmptyParameters(params))
        {
            /*
            AlgorithmParameters  sigParams = AlgorithmParameters.getInstance(signature.getAlgorithm(), signature.getProvider());
            
            try
            {
                sigParams.init(params.getDERObject().getDEREncoded());
            }
            catch (IOException e)
            {
                throw new SignatureException("IOException decoding parameters: " + e.getMessage());
            }

            try
            {
                signature.setParameters(sigParams.getParameterSpec(PSSParameterSpec.class));
            }
            catch (GeneralSecurityException e)
            {
                throw new SignatureException("Exception extracting parameters: " + e.getMessage());
            }
            */
        }
    }

    static String getSignatureName(AlgorithmIdentifier sigAlgId) 
    {
        ASN1ObjectIdentifier sigAlgOid = sigAlgId.getAlgorithm();
        ASN1Encodable params = sigAlgId.getParameters();

        if (!isAbsentOrEmptyParameters(params))
        {
            if (PKCSObjectIdentifiers.id_RSASSA_PSS.equals(sigAlgOid))
           {
                RSASSAPSSparams rsaParams = RSASSAPSSparams.getInstance(params);

                return getDigestAlgName(rsaParams.getHashAlgorithm().getAlgorithm()) + "withRSAandMGF1";
            }
            if (X9ObjectIdentifiers.ecdsa_with_SHA2.equals(sigAlgOid))
            {
                AlgorithmIdentifier ecDsaParams = AlgorithmIdentifier.getInstance(params);

                return getDigestAlgName(ecDsaParams.getAlgorithm()) + "withECDSA";
            }
        }

        return sigAlgOid.getId();
    }

    /**
     * Return the digest algorithm using one of the standard JCA string
     * representations rather the the algorithm identifier (if possible).
     */
    private static String getDigestAlgName(
        ASN1ObjectIdentifier digestAlgOID)
    {
        if (PKCSObjectIdentifiers.md5.equals(digestAlgOID))
        {
            return "MD5";
        }
        else if (OIWObjectIdentifiers.idSHA1.equals(digestAlgOID))
        {
            return "SHA1";
        }
        else if (NISTObjectIdentifiers.id_sha224.equals(digestAlgOID))
        {
            return "SHA224";
        }
        else if (NISTObjectIdentifiers.id_sha256.equals(digestAlgOID))
        {
            return "SHA256";
        }
        else if (NISTObjectIdentifiers.id_sha384.equals(digestAlgOID))
        {
            return "SHA384";
        }
        else if (NISTObjectIdentifiers.id_sha512.equals(digestAlgOID))
        {
            return "SHA512";
        }
        else if (TeleTrusTObjectIdentifiers.ripemd128.equals(digestAlgOID))
        {
            return "RIPEMD128";
        }
        else if (TeleTrusTObjectIdentifiers.ripemd160.equals(digestAlgOID))
        {
            return "RIPEMD160";
        }
        else if (TeleTrusTObjectIdentifiers.ripemd256.equals(digestAlgOID))
        {
            return "RIPEMD256";
        }
        else if (CryptoProObjectIdentifiers.gostR3411.equals(digestAlgOID))
        {
            return "GOST3411";
        }
        else
        {
            return digestAlgOID.getId();            
        }
    }
}
