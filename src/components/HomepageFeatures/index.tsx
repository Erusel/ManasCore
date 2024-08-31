import clsx from 'clsx';
import Heading from '@theme/Heading';
import styles from './styles.module.css';

type FeatureItem = {
    title: string;
    description: JSX.Element;
};

const FeatureList: FeatureItem[] = [
    {
        title: 'Easy to Use',
        description: (
            <>
                All features are designed to be as easy to be used as possible.
                We use this library in our own projects, so we find and fix pain points probably everyone has.
            </>
        ),
    },
    {
        title: 'Modular',
        description: (
            <>
                Not every project needs all features.
                That's why we pack every feature into their own module.
            </>
        ),
    },
    {
        title: 'Available for Architectury, Fabric and NeoForge ',
        description: (
            <>
                Every feature is available and tested for Architectury, Fabric and NeoForge.
            </>
        ),
    },
];

function Feature({title, description}: FeatureItem) {
    return (
        <div className={clsx('col col--4')}>
            <div className="text--center padding-horiz--md">
                <Heading as="h3">{title}</Heading>
                <p>{description}</p>
            </div>
        </div>
    );
}

export default function HomepageFeatures(): JSX.Element {
    return (
        <section className={styles.features}>
            <div className="container">
                <div className="row">
                    {FeatureList.map((props, idx) => (
                        <Feature key={idx} {...props} />
                    ))}
                </div>
            </div>
        </section>
    );
}
