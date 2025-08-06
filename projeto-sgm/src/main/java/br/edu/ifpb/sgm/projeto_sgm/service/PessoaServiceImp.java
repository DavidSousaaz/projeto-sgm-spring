package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.PessoaRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.PessoaResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.exception.InstituicaoNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.exception.PessoaNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.mapper.PessoaMapper;
import br.edu.ifpb.sgm.projeto_sgm.model.Instituicao;
import br.edu.ifpb.sgm.projeto_sgm.model.Pessoa;
import br.edu.ifpb.sgm.projeto_sgm.repository.AlunoRepository;
import br.edu.ifpb.sgm.projeto_sgm.repository.InstituicaoRepository;
import br.edu.ifpb.sgm.projeto_sgm.repository.PessoaRepository;
import br.edu.ifpb.sgm.projeto_sgm.repository.ProfessorRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PessoaServiceImp implements PessoaService {

    private final PessoaRepository pessoaRepository;
    private final InstituicaoRepository instituicaoRepository;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final PessoaMapper pessoaMapper;
    private final PasswordEncoder passwordEncoder;

    public PessoaServiceImp(PessoaRepository pessoaRepository, InstituicaoRepository instituicaoRepository, AlunoRepository alunoRepository, ProfessorRepository professorRepository, PessoaMapper pessoaMapper, PasswordEncoder passwordEncoder) {
        this.pessoaRepository = pessoaRepository;
        this.instituicaoRepository = instituicaoRepository;
        this.alunoRepository = alunoRepository;
        this.professorRepository = professorRepository;
        this.pessoaMapper = pessoaMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PessoaResponseDTO save(PessoaRequestDTO dto) {
        Pessoa pessoa = pessoaMapper.toEntity(dto);
        if (dto.getInstituicaoId() != null) {
            pessoa.setInstituicao(buscarInstituicao(dto.getInstituicaoId()));
        }
        pessoa.setSenha(passwordEncoder.encode(dto.getSenha()));

        Pessoa savedPessoa = pessoaRepository.save(pessoa);
        return pessoaMapper.toResponseDTO(savedPessoa);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PessoaResponseDTO> findAll() {
        return pessoaRepository.findAll().stream()
                .map(pessoaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PessoaResponseDTO findById(Long id) {
        return pessoaRepository.findById(id)
                .map(pessoaMapper::toResponseDTO)
                .orElseThrow(() -> new PessoaNotFoundException("Pessoa com ID " + id + " não encontrada."));
    }

    @Override
    public PessoaResponseDTO update(Long id, PessoaRequestDTO dto) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new PessoaNotFoundException("Pessoa com ID " + id + " não encontrada para atualização."));

        pessoaMapper.updatePessoaFromDto(dto, pessoa);

        if (dto.getInstituicaoId() != null) {
            pessoa.setInstituicao(buscarInstituicao(dto.getInstituicaoId()));
        }
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            pessoa.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        Pessoa updatedPessoa = pessoaRepository.save(pessoa);
        return pessoaMapper.toResponseDTO(updatedPessoa);
    }

    @Override
    public void delete(Long id) {
        if (!pessoaRepository.existsById(id)) {
            throw new PessoaNotFoundException("Pessoa com ID " + id + " não encontrada para deleção.");
        }

        // Tenta desativar o perfil de Aluno, se existir
        alunoRepository.findById(id).ifPresent(aluno -> {
            aluno.setCadastrado(false);
            alunoRepository.save(aluno);
        });

        // Tenta desativar o perfil de Professor, se existir
        professorRepository.findById(id).ifPresent(professor -> {
            professor.setCadastrado(false);
            professorRepository.save(professor);
        });

        // Só deleta a Pessoa fisicamente se ela NÃO tiver perfil de aluno ou professor
        // Usamos existsById de novo para ter certeza
        if (!alunoRepository.existsById(id) && !professorRepository.existsById(id)) {
            pessoaRepository.deleteById(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PessoaResponseDTO findDtoByMatricula(String matricula) {
        return pessoaRepository.findByMatricula(matricula)
                .map(pessoaMapper::toResponseDTO)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário com matrícula '" + matricula + "' não encontrado."));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String matricula) throws UsernameNotFoundException {
        return pessoaRepository.findByMatricula(matricula)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário com matrícula '" + matricula + "' não encontrado."));
    }

    private Instituicao buscarInstituicao(Long id) {
        return instituicaoRepository.findById(id)
                .orElseThrow(() -> new InstituicaoNotFoundException("Instituição com ID " + id + " não encontrada."));
    }
}