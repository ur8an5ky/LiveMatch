-- =========================================
-- LiveMatch — initial seed data
-- Idempotent: only inserts if tables are empty
-- =========================================

-- Teams (with custom codes for UK home nations)
INSERT INTO teams (name, short_name, country)
SELECT v.name, v.short_name, v.country
FROM (VALUES
          -- Spain (ES)
          ('FC Barcelona', 'FCB', 'ES'),
          ('Real Madrid', 'RMA', 'ES'),
          ('Atlético Madrid', 'ATM', 'ES'),
          ('Athletic Bilbao', 'ATH', 'ES'),
          -- England (ENG — custom for UK home nation)
          ('Manchester City', 'MCI', 'ENG'),
          ('Liverpool', 'LIV', 'ENG'),
          ('Arsenal', 'ARS', 'ENG'),
          ('Manchester United', 'MUN', 'ENG'),
          ('Chelsea', 'CHE', 'ENG'),
          -- Scotland (SCT)
          ('Celtic', 'CEL', 'SCT'),
          ('Rangers', 'RAN', 'SCT'),
          -- Wales (WLS)
          ('Cardiff City', 'CDF', 'WLS'),
          -- Northern Ireland (NIR)
          ('Linfield', 'LIN', 'NIR'),
          -- Germany (DE)
          ('Bayern Munich', 'BAY', 'DE'),
          ('Borussia Dortmund', 'BVB', 'DE'),
          ('RB Leipzig', 'RBL', 'DE'),
          -- France (FR)
          ('Paris Saint-Germain', 'PSG', 'FR'),
          ('Olympique Marseille', 'OM', 'FR'),
          -- Italy (IT)
          ('Juventus', 'JUV', 'IT'),
          ('AC Milan', 'MIL', 'IT'),
          ('Inter Milan', 'INT', 'IT'),
          -- Poland (PL)
          ('Legia Warszawa', 'LEG', 'PL'),
          ('Lech Poznań', 'LEC', 'PL'),
          -- Portugal (PT)
          ('Benfica', 'BEN', 'PT'),
          ('FC Porto', 'POR', 'PT'),
          -- Netherlands (NL)
          ('Ajax Amsterdam', 'AJA', 'NL'),
          ---------------------------------------------------------
          ---------------------------------------------------------
          --                   National Teams                    --
          ---------------------------------------------------------
          ---------------------------------------------------------
          -- South America (CONMEBOL)
          (NULL, 'ARG', 'AR'),  -- Argentina
          (NULL, 'BRA', 'BR'),  -- Brazil
          (NULL, 'URU', 'UY'),  -- Uruguay
          (NULL, 'COL', 'CO'),  -- Colombia
          (NULL, 'CHI', 'CL'),  -- Chile
          (NULL, 'ECU', 'EC'),  -- Ecuador
          (NULL, 'PAR', 'PY'),  -- Paraguay
          (NULL, 'PER', 'PE'),  -- Peru
          -- Europe (UEFA)
          (NULL, 'GER', 'DE'),  -- Germany
          (NULL, 'FRA', 'FR'),  -- France
          (NULL, 'ESP', 'ES'),  -- Spain
          (NULL, 'ITA', 'IT'),  -- Italy
          (NULL, 'NED', 'NL'),  -- Netherlands
          (NULL, 'BEL', 'BE'),  -- Belgium
          (NULL, 'POR', 'PT'),  -- Portugal
          (NULL, 'CRO', 'HR'),  -- Croatia
          (NULL, 'POL', 'PL'),  -- Poland
          (NULL, 'SUI', 'CH'),  -- Switzerland
          (NULL, 'DEN', 'DK'),  -- Denmark
          (NULL, 'SWE', 'SE'),  -- Sweden
          (NULL, 'NOR', 'NO'),  -- Norway
          (NULL, 'SRB', 'RS'),  -- Serbia
          (NULL, 'AUT', 'AT'),  -- Austria
          (NULL, 'CZE', 'CZ'),  -- Czechia
          (NULL, 'UKR', 'UA'),  -- Ukraine
          (NULL, 'TUR', 'TR'),  -- Turkey
          (NULL, 'BIH', 'BA'),  -- Bosnia and Herzegovina
          -- UK home nations (custom codes for both shortName and country)
          (NULL, 'ENG', 'ENG'), -- England
          (NULL, 'SCO', 'SCT'), -- Scotland
          (NULL, 'WAL', 'WLS'), -- Wales
          (NULL, 'NIR', 'NIR'), -- Northern Ireland
          -- North/Central America (CONCACAF)
          (NULL, 'USA', 'US'),  -- USA
          (NULL, 'MEX', 'MX'),  -- Mexico
          (NULL, 'CAN', 'CA'),  -- Canada
          (NULL, 'HTI', 'HT'),  -- Haiti
          (NULL, 'CUW', 'CW'),  -- Curacao
          (NULL, 'CRC', 'CR'),  -- Costa Rica
          (NULL, 'PAN', 'PA'),  -- Panama
          -- Asia (AFC)
          (NULL, 'JPN', 'JP'),  -- Japan
          (NULL, 'KOR', 'KR'),  -- South Korea
          (NULL, 'AUS', 'AU'),  -- Australia
          (NULL, 'IRN', 'IR'),  -- Iran
          (NULL, 'IRQ', 'IQ'),  -- Iraq
          (NULL, 'QAT', 'QA'),  -- Qatar
          (NULL, 'KSA', 'SA'),  -- Saudi Arabia
          (NULL, 'JOR', 'JO'),  -- Jordan
          (NULL, 'UZB', 'UZ'),  -- Uzbekistan
          -- Africa (CAF)
          (NULL, 'SEN', 'SN'),  -- Senegal
          (NULL, 'MAR', 'MA'),  -- Morocco
          (NULL, 'TUN', 'TN'),  -- Tunisia
          (NULL, 'CMR', 'CM'),  -- Cameroon
          (NULL, 'NGA', 'NG'),  -- Nigeria
          (NULL, 'GHA', 'GH'),  -- Ghana
          (NULL, 'EGY', 'EG'),  -- Egypt
          (NULL, 'CPV', 'CV'),  -- Cabo Verde
          (NULL, 'CIV', 'CI'),  -- Ivory Coast
          (NULL, 'RSA', 'ZA'),  -- South Africa
          (NULL, 'DZA', 'DZ'),  -- Algeria
          (NULL, 'COD', 'CD'),  -- DR Congo
          -- Oceania (OFC)
          (NULL, 'NZL', 'NZ')   -- New Zealand
     ) AS v(name, short_name, country)
WHERE NOT EXISTS (SELECT 1 FROM teams);

-- =========================================
-- FIFA World Cup 2026 — Group Stage Schedule
-- All 72 group-stage matches with real fixtures and results where known
-- Times are in Polish time (CEST, UTC+2)
-- =========================================
INSERT INTO matches (home_team_id, away_team_id, start_time, status, home_score, away_score)
SELECT
    (SELECT id FROM teams WHERE short_name = v.home AND name IS NULL),
    (SELECT id FROM teams WHERE short_name = v.away AND name IS NULL),
    v.start_time::timestamp,
    v.status,
    v.home_score,
    v.away_score
FROM (VALUES
          -- ============================================
          -- MATCHDAY 1 (Jun 11-17)
          -- ============================================
          -- Group A
          ('MEX', 'RSA', '2026-06-11 20:00:00', 'FINISHED', 2, 0),
          ('KOR', 'CZE', '2026-06-12 18:00:00', 'FINISHED', 2, 1),
          -- Group B
          ('CAN', 'BIH', '2026-06-12 21:00:00', 'FINISHED', 1, 1),
          ('QAT', 'SUI', '2026-06-13 18:00:00', 'FINISHED', 1, 1),
          -- Group D
          ('USA', 'PAR', '2026-06-12 23:30:00', 'FINISHED', 4, 1),
          ('AUS', 'TUR', '2026-06-14 02:00:00', 'FINISHED', 2, 0),
          -- Group C
          ('BRA', 'MAR', '2026-06-13 21:00:00', 'FINISHED', 1, 1),
          ('SCO', 'HTI', '2026-06-13 23:30:00', 'FINISHED', 1, 0),
          -- Group E
          ('GER', 'CUW', '2026-06-14 18:00:00', 'FINISHED', 7, 1),
          ('CIV', 'ECU', '2026-06-14 23:30:00', 'FINISHED', 1, 0),
          -- Group F
          ('NED', 'JPN', '2026-06-14 21:00:00', 'FINISHED', 2, 2),
          ('SWE', 'TUN', '2026-06-15 02:00:00', 'FINISHED', 5, 1),
          -- Group H
          ('ESP', 'CPV', '2026-06-15 18:00:00', 'FINISHED', 0, 0),
          ('KSA', 'URU', '2026-06-15 23:30:00', 'FINISHED', 1, 1),
          -- Group G
          ('BEL', 'EGY', '2026-06-15 21:00:00', 'FINISHED', 1, 1),
          ('IRN', 'NZL', '2026-06-16 02:00:00', 'FINISHED', 2, 2),
          -- Group I
          ('FRA', 'SEN', '2026-06-16 21:00:00', 'FINISHED', 3, 1),
          ('NOR', 'IRQ', '2026-06-16 23:30:00', 'FINISHED', 4, 1),
          -- Group J
          ('ARG', 'DZA', '2026-06-17 02:00:00', 'FINISHED', 3, 0),
          ('AUT', 'JOR', '2026-06-17 05:00:00', 'FINISHED', 3, 1),
          -- Group K
          ('POR', 'COD', '2026-06-17 18:00:00', 'FINISHED', 1, 1),
          ('COL', 'UZB', '2026-06-18 03:00:00', 'FINISHED', 3, 1),
          -- Group L
          ('ENG', 'CRO', '2026-06-17 21:00:00', 'FINISHED', 4, 2),
          ('GHA', 'PAN', '2026-06-18 00:00:00', 'FINISHED', 1, 0),

          -- ============================================
          -- MATCHDAY 2 (Jun 18-23)
          -- ============================================
          -- Group A
          ('CZE', 'RSA', '2026-06-18 18:00:00', 'FINISHED', 1, 1),
          ('MEX', 'KOR', '2026-06-19 02:00:00', 'FINISHED', 1, 1),
          -- Group B
          ('SUI', 'BIH', '2026-06-18 21:00:00', 'FINISHED', 4, 1),
          ('CAN', 'QAT', '2026-06-19 00:00:00', 'FINISHED', 6, 0),
          -- Group D
          ('USA', 'AUS', '2026-06-19 20:00:00', 'FINISHED', 2, 0),
          ('TUR', 'PAR', '2026-06-20 04:00:00', 'FINISHED', 0, 1),
          -- Group C
          ('SCO', 'MAR', '2026-06-19 23:00:00', 'FINISHED', 0, 1),
          ('BRA', 'HTI', '2026-06-20 01:30:00', 'FINISHED', 3, 0),
          -- Group F
          ('NED', 'SWE', '2026-06-20 18:00:00', 'FINISHED', 5, 1),
          ('JPN', 'TUN', '2026-06-21 05:00:00', 'FINISHED', 4, 0),
          -- Group E
          ('GER', 'CIV', '2026-06-20 21:00:00', 'FINISHED', 2, 1),
          ('ECU', 'CUW', '2026-06-21 01:00:00', 'FINISHED', 0, 0),
          -- Group H (LIVE today!)
          ('ESP', 'KSA', '2026-06-21 17:30:00', 'SCHEDULED', 1, 0),
          ('URU', 'CPV', '2026-06-21 23:00:00', 'SCHEDULED', 0, 0),
          -- Group G
          ('BEL', 'IRN', '2026-06-21 20:00:00', 'SCHEDULED', 0, 0),
          ('NZL', 'EGY', '2026-06-22 02:00:00', 'SCHEDULED', 0, 0),
          -- Group J
          ('ARG', 'AUT', '2026-06-22 18:00:00', 'SCHEDULED', 0, 0),
          ('JOR', 'DZA', '2026-06-23 04:00:00', 'SCHEDULED', 0, 0),
          -- Group I
          ('FRA', 'IRQ', '2026-06-22 22:00:00', 'SCHEDULED', 0, 0),
          ('NOR', 'SEN', '2026-06-23 01:00:00', 'SCHEDULED', 0, 0),
          -- Group K
          ('POR', 'UZB', '2026-06-23 18:00:00', 'SCHEDULED', 0, 0),
          ('COL', 'COD', '2026-06-24 03:00:00', 'SCHEDULED', 0, 0),
          -- Group L
          ('ENG', 'GHA', '2026-06-23 21:00:00', 'SCHEDULED', 0, 0),
          ('PAN', 'CRO', '2026-06-24 00:00:00', 'SCHEDULED', 0, 0),

          -- ============================================
          -- MATCHDAY 3 (Jun 24-27)
          -- ============================================
          -- Group B
          ('SUI', 'CAN', '2026-06-24 20:00:00', 'SCHEDULED', 0, 0),
          ('BIH', 'QAT', '2026-06-24 20:00:00', 'SCHEDULED', 0, 0),
          -- Group C
          ('MAR', 'HTI', '2026-06-24 23:00:00', 'SCHEDULED', 0, 0),
          ('SCO', 'BRA', '2026-06-24 23:00:00', 'SCHEDULED', 0, 0),
          -- Group A
          ('RSA', 'KOR', '2026-06-25 02:00:00', 'SCHEDULED', 0, 0),
          ('CZE', 'MEX', '2026-06-25 02:00:00', 'SCHEDULED', 0, 0),
          -- Group E
          ('CUW', 'CIV', '2026-06-25 21:00:00', 'SCHEDULED', 0, 0),
          ('ECU', 'GER', '2026-06-25 21:00:00', 'SCHEDULED', 0, 0),
          -- Group F
          ('JPN', 'SWE', '2026-06-26 00:00:00', 'SCHEDULED', 0, 0),
          ('TUN', 'NED', '2026-06-26 00:00:00', 'SCHEDULED', 0, 0),
          -- Group D
          ('TUR', 'USA', '2026-06-26 03:00:00', 'SCHEDULED', 0, 0),
          ('PAR', 'AUS', '2026-06-26 03:00:00', 'SCHEDULED', 0, 0),
          -- Group I
          ('NOR', 'FRA', '2026-06-26 20:00:00', 'SCHEDULED', 0, 0),
          ('SEN', 'IRQ', '2026-06-26 20:00:00', 'SCHEDULED', 0, 0),
          -- Group H
          ('CPV', 'KSA', '2026-06-27 01:00:00', 'SCHEDULED', 0, 0),
          ('URU', 'ESP', '2026-06-27 01:00:00', 'SCHEDULED', 0, 0),
          -- Group G
          ('NZL', 'BEL', '2026-06-27 04:00:00', 'SCHEDULED', 0, 0),
          ('EGY', 'IRN', '2026-06-27 04:00:00', 'SCHEDULED', 0, 0),
          -- Group L
          ('PAN', 'ENG', '2026-06-27 22:00:00', 'SCHEDULED', 0, 0),
          ('CRO', 'GHA', '2026-06-27 22:00:00', 'SCHEDULED', 0, 0),
          -- Group K
          ('COL', 'POR', '2026-06-28 00:30:00', 'SCHEDULED', 0, 0),
          ('COD', 'UZB', '2026-06-28 00:30:00', 'SCHEDULED', 0, 0),
          -- Group J
          ('DZA', 'AUT', '2026-06-28 03:00:00', 'SCHEDULED', 0, 0),
          ('JOR', 'ARG', '2026-06-28 03:00:00', 'SCHEDULED', 0, 0)
     ) AS v(home, away, start_time, status, home_score, away_score)
WHERE NOT EXISTS (SELECT 1 FROM matches);